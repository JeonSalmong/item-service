package apps.itemservice.web.domain.entity.item;

import apps.itemservice.platform.exception.NotEnoughStockException;
import apps.itemservice.web.domain.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.NumberFormat;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Data
public class Item extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "공백은 입력할 수 없습니다.(필수)")
    private String itemName;
    //@NotNull
    //@Range(min = 1000, max = 100000)
    @NumberFormat(pattern = "###,###")
    private Integer price;
    //@NotNull
    //@Max(9999)
    @NumberFormat(pattern = "###,###")
    private Integer quantity;

    @Transient
    private ItemFile attachFile;

    @Transient
    private List<ItemFile> imageFiles;

    @ManyToMany(mappedBy = "items")                         //**
    private List<Category> categories = new ArrayList<Category>(); //**

    public Item() {
    }
    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    //==Biz Method==//
    public void addStock(int quantity) {
        this.quantity += quantity;
    }

    public void removeStock(int quantity) {
        int restStock = this.quantity - quantity;
        if (restStock < 0) {
            throw new NotEnoughStockException("need more stock");
        }
        this.quantity = restStock;
    }

}
