package io.oigres.ecomm.service.orders.domain;

import java.time.LocalDateTime;

public interface Auditable {
    static public final String DELETED_AT_PROPERTY_NAME = "deletedAt";

    LocalDateTime getCreatedAt();
    void setCreatedAt(LocalDateTime createdAt);
    String getCreatedBy();
    void setCreatedBy(String createdBy);

    LocalDateTime getModifiedAt();
    void setModifiedAt(LocalDateTime modifiedAt);
    String getModifiedBy();
    void setModifiedBy(String modifiedBy);

    LocalDateTime getDeletedAt();
    void setDeletedAt(LocalDateTime deletedAt);
    String getDeletedBy();
    void setDeletedBy(String deletedBy);

    default boolean isDeleted() {
        return getDeletedAt() != null;
    }

    default public void delete() {
        setDeletedAt(LocalDateTime.now());
    }

}
