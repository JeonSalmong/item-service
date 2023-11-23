package apps.itemservice.repository.order;

import apps.itemservice.domain.entity.member.Member;
import apps.itemservice.domain.entity.order.OrderSearch;
import apps.itemservice.domain.entity.order.Orders;
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

    public JpaOrderRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Orders save(Orders order) {
        em.persist(order);
        return order;
    }

    @Override
    public Orders fineOne(Long id) {
        return em.find(Orders.class, id);
    }

    @Override
    public List<Orders> findAll(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Orders> cq = cb.createQuery(Orders.class);
        Root<Orders> o = cq.from(Orders.class);

        List<Predicate> criteria = new ArrayList<Predicate>();

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
        return query.getResultList();
    }
}
