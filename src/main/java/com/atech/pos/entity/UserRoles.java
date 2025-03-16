package com.atech.pos.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
public class UserRoles {

    private boolean canEditRole;

    private boolean canAddUser;
    private boolean canEditUser;
    private boolean canViewUser;
    private boolean canViewUserList;
    private boolean canDeleteUser;

    private boolean canAddProduct;
    private boolean canEditProduct;
    private boolean canViewProduct;
    private boolean canViewProductList;
    private boolean canDeleteProduct;

    private boolean canAddCategory;
    private boolean canEditCategory;
    private boolean canViewCategory;
    private boolean canViewCategoryList;
    private boolean canDeleteCategory;
}
