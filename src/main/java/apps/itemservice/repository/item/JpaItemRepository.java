package apps.itemservice.repository.item;

import apps.itemservice.domain.entity.item.Item;
import apps.itemservice.domain.entity.item.ItemFile;
import apps.itemservice.domain.vo.CommonCode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

public class JpaItemRepository implements ItemRepository {

    private final EntityManager em;

    public JpaItemRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Item save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            //em.merge(item); // update를 위한 처리 but!! 이미지처리 때문에 update 분리 함
        }

        // 첨부파일명 저장
        if (item.getAttachFile() != null) {
            ItemFile itemFile = new ItemFile();
            itemFile.setItemId(item.getId());
            itemFile.setStoreFileName(item.getAttachFile().getStoreFileName());
            itemFile.setUploadFileName(item.getAttachFile().getUploadFileName());
            itemFile.setType(String.valueOf(CommonCode.ATTACH_FILE_TYPE.getCode()));
            itemFileSave(itemFile);
        }

        // 첨부이미지 저장
        if (item.getImageFiles() != null) {
            for (ItemFile uploadFile : item.getImageFiles()) {
                ItemFile itemFile = new ItemFile();
                itemFile.setItemId(item.getId());
                itemFile.setStoreFileName(uploadFile.getStoreFileName());
                itemFile.setUploadFileName(uploadFile.getUploadFileName());
                itemFile.setType(String.valueOf(CommonCode.IMAGES_FILE_TYPE.getCode()));
                itemFileSave(itemFile);
            }
        }

        return item;
    }

    private void itemFileSave(ItemFile itemFile) {
        em.persist(itemFile);
    }

    @Override
    public Item findById(Long id) {
        Item item = em.find(Item.class, id);
        List<ItemFile> itemFiles = findItemFiles(id);
        List<ItemFile> uploadFiles = new ArrayList<>();
        for (ItemFile itemFile : itemFiles) {
            if (itemFile != null) {
                if (CommonCode.ATTACH_FILE_TYPE.getCode().equals(itemFile.getType())) {
                    // attach 파일정보
                    ItemFile uploadFile = new ItemFile();
                    uploadFile.setStoreFileName(itemFile.getStoreFileName());
                    uploadFile.setUploadFileName(itemFile.getUploadFileName());
                    item.setAttachFile(uploadFile);
                } else {
                    // images 파일 정보
                    ItemFile uploadFile = new ItemFile();
                    uploadFile.setStoreFileName(itemFile.getStoreFileName());
                    uploadFile.setUploadFileName(itemFile.getUploadFileName());
                    uploadFiles.add(uploadFile);
                }
            }
        }

        if (!uploadFiles.isEmpty()) {
            item.setImageFiles(uploadFiles);
        }
        return item;
    }

    private List<ItemFile> findItemFiles(Long id) {
        return em.createQuery("select i from ItemFile i where i.itemId = :id", ItemFile.class)
                .setParameter("id", id)
                .getResultList();
    }

    private ItemFile findItemFile(Long seq, Long itemId) {
        ItemFile itemFile = null;
        try {
            itemFile = em.createQuery("select i from ItemFile i where i.seq = :seq and i.itemId = :itemId", ItemFile.class)
                        .setParameter("seq", seq)
                        .setParameter("itemId", itemId)
                        .getSingleResult();
        } catch (NoResultException e) {
            itemFile = null;
        }
        finally {
            return itemFile;
        }
    }

    @Override
    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }

    @Override
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());

        // 첨부파일명 업데이트
        if (updateParam.getAttachFile() != null) {
            ItemFile itemFile = findItemFile(updateParam.getAttachFile().getSeq(), itemId);
            if (itemFile != null) {
                itemFile.setStoreFileName(updateParam.getAttachFile().getStoreFileName());
                itemFile.setUploadFileName(updateParam.getAttachFile().getUploadFileName());
            } else {
                ItemFile insertItemFile = new ItemFile();
                insertItemFile.setSeq(updateParam.getAttachFile().getSeq());
                insertItemFile.setItemId(itemId);
                insertItemFile.setType(CommonCode.ATTACH_FILE_TYPE.getCode());
                insertItemFile.setStoreFileName(updateParam.getAttachFile().getStoreFileName());
                insertItemFile.setUploadFileName(updateParam.getAttachFile().getUploadFileName());
                em.merge(insertItemFile);
            }

        }

        // 첨부이미지 업데이트
        if (updateParam.getImageFiles() != null) {
            for (ItemFile updateFile : updateParam.getImageFiles()) {
                ItemFile itemFile = findItemFile(updateFile.getSeq(), itemId);
                if (itemFile != null) {
                    itemFile.setStoreFileName(updateFile.getStoreFileName());
                    itemFile.setUploadFileName(updateFile.getUploadFileName());
                } else {
                    ItemFile insertItemFile = new ItemFile();
                    insertItemFile.setSeq(updateFile.getSeq());
                    insertItemFile.setItemId(itemId);
                    insertItemFile.setType(CommonCode.IMAGES_FILE_TYPE.getCode());
                    insertItemFile.setStoreFileName(updateFile.getStoreFileName());
                    insertItemFile.setUploadFileName(updateFile.getUploadFileName());
                    em.merge(insertItemFile);
                }
            }
        }
    }
}
