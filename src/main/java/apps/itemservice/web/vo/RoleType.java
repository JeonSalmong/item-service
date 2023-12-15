package apps.itemservice.web.vo;

public enum RoleType {
    USER(RoleType.Authority.USER),  // 사용자 권한
    ADMIN(RoleType.Authority.ADMIN);  // 관리자 권한

    private final String authority;

    RoleType(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }
}
