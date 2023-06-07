package my.app.advertzone.model.user;

public enum AccountRole {

    NORMAL_USER("NormalUser"),
    ADMIN("Admin"),
    BANNED("Banned");

    private final String roleName;

    AccountRole(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "AccountRole{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}