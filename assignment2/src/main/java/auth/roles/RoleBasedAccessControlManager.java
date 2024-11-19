package auth.roles;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RoleBasedAccessControlManager implements IAccessControl {

  private Map<String, Set<String>> roleHierarchy; // Role hierarchy (role -> set of inherited roles)
  private Map<String, Set<String>> rolePermissions; // Permissions for each role
  private Map<String, String> userRoles; // Roles assigned to each user

  public RoleBasedAccessControlManager(String rolesFile, String hierarchyFile, String userRolesFile,
      String permissionsFile) {
    roleHierarchy = new HashMap<>();
    rolePermissions = new HashMap<>();
    userRoles = new HashMap<>();

    loadRoles(rolesFile);
    loadHierarchy(hierarchyFile);
    loadUserRoles(userRolesFile);
    loadPermissions(permissionsFile);
  }

  private void loadRoles(String rolesFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(rolesFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        roleHierarchy.put(line.trim(), new HashSet<>());
      }
    } catch (IOException e) {
      System.err.println("Could not read roles file: " + e);
    }
  }

  private void loadHierarchy(String hierarchyFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(hierarchyFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split("->");
        if (parts.length == 2) {
          String parent = parts[0].trim();
          String child = parts[1].trim();
          roleHierarchy.get(parent).add(child);
        }
      }
    } catch (IOException e) {
      System.err.println("Could not read hierarchy file: " + e);
    }
  }

  private void loadUserRoles(String userRolesFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(userRolesFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
          String user = parts[0].trim();
          String roles = parts[1].trim();
          userRoles.put(user, roles);
        }
      }
    } catch (IOException e) {
      System.err.println("Could not read user roles file: " + e);
    }
  }

  private void loadPermissions(String permissionsFile) {
    try (BufferedReader reader = new BufferedReader(new FileReader(permissionsFile))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(":");
        if (parts.length == 2) {
          String role = parts[0].trim();
          String[] permissions = parts[1].split(",");
          rolePermissions.put(role, new HashSet<>(Arrays.asList(permissions)));
        }
      }
    } catch (IOException e) {
      System.err.println("Could not read permissions file: " + e);
    }
  }

  public boolean check(String username, String resource) throws MissingRequiredAccessException {
    String userAssignedRole = userRoles.get(username);
    if (userAssignedRole == null) {
      throw new MissingRequiredAccessException("User " + username + " has no roles.");
    }

    Set<String> effectiveRoles = new HashSet<>();
    expandRole(userAssignedRole, effectiveRoles);

    for (String role : effectiveRoles) {
      Set<String> permissions = rolePermissions.get(role);
      if (permissions != null && permissions.contains(resource)) {
        return true;
      }
    }

    throw new MissingRequiredAccessException(
        "Resource: " + resource + " is not allowed for user roles: " + String.join(", ", userAssignedRole));
  }

  private void expandRole(String role, Set<String> effectiveRoles) {
    if (!effectiveRoles.contains(role)) {
      effectiveRoles.add(role);

      Set<String> inheritedRoles = roleHierarchy.get(role);
      if (inheritedRoles != null) {
        for (String inheritedRole : inheritedRoles) {
          expandRole(inheritedRole, effectiveRoles);
        }
      }
    }
  }

}
