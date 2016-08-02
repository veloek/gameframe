package gameframe;

import java.security.*;

/**
 * SandboxSecurityPolicy
 *
 * Security policy for GF games (sandboxing)
 *
 * @author Vegard Løkken <vegard@loekken.org>
 */
public class SandboxSecurityPolicy extends Policy {
    @Override
    public PermissionCollection getPermissions(ProtectionDomain domain) {
        if (isGFGame(domain)) {
            return gamePermissions();
        } else {
            return applicationPermissions();
        }
    }

    private boolean isGFGame(ProtectionDomain domain) {
        return domain.getClassLoader() instanceof GFClassLoader;
    }

    private PermissionCollection gamePermissions() {
        Permissions permissions = new Permissions(); // No permissions
        return permissions;
    }

    private PermissionCollection applicationPermissions() {
        Permissions permissions = new Permissions();
        permissions.add(new AllPermission());
        return permissions;
    }
}
