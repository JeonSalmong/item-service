package apps.itemservice.web.domain.order;

import apps.itemservice.config.aop.trace.LogTrace;
import apps.itemservice.config.aop.trace.TraceStatus;
import apps.itemservice.config.aop.trace.template.AbstractTemplate;
import apps.itemservice.config.aop.trace.template.TraceTemplate;
import apps.itemservice.web.domain.entity.member.Member;
import apps.itemservice.web.domain.entity.order.OrderSearch;
import apps.itemservice.web.domain.entity.order.Orders;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class JpaOrderRepository implements OrderRepository {
    EntityManager em;
    private final LogTrace trace;
    private final TraceTemplate template;

    public JpaOrderRepository(EntityManager em, LogTrace trace, TraceTemplate template) {
        this.em = em;
        this.trace = trace;
        this.template = template;
    }

    @Override
    public Orders save(Orders order) {
        AbstractTemplate<Orders> template = new AbstractTemplate<Orders>(trace) {
            @Override
            protected Orders call() {
                em.persist(order);
                return order;
            }
        };
        return template.execute("JpaOrderRepository.save()");
    }

    @Override
    public Orders fineOne(Long id) {
        return template.execute("JpaOrderRepository.fineOne()", () -> {
            return em.find(Orders.class, id);
        });
    }

    @Override
    public List<Orders> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> o = cq.from(Orders.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

        TraceStatus statusT = null;
        try {
            statusT = trace.begin("JpaOrderRepository.findAll()");

            //주문 상태 검색
            if (orderSearch.getOrderStatus() != null) {
                Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
                criteria.add(status);
            }
            //회원 이름 검색
            if (StringUtils.hasText(orderSearch.getMemberName())) {
                Join<Orders, Member> m = o.join("member", JoinType.INNER); //회원과 조인
                Predicate name = cb.like(m.<String>get("name"), "%" + orderSearch.getMemberName() + "%");
                criteria.add(name);
            }

            cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
            TypedQuery<Orders> query = em.createQuery(cq).setMaxResults(1000); //최대 검색 1000 건으로 제한
            List<Orders> orders = new ArrayList<>();
            orders = query.getResultList();
            sleep(1000);
            trace.end(statusT);
            return orders;
        } catch (Exception e) {
            trace.exception(statusT, e);
            throw e;
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
