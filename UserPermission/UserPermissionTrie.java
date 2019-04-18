
import java.util.*;

public class UserPermissionTrie {
    protected final Map<Character,UserPermissionTrie> children;
    protected final Set<Integer> permission; // string so far
    protected final Character id;


    public UserPermissionTrie(Character id, Set<Integer> permission){
        this.id = id;
        this.permission = permission;
        children = new HashMap<>();

    }

    protected UserPermissionTrie addChild(char c, Set<Integer> permission){
        UserPermissionTrie newNode = new UserPermissionTrie(c,permission);
        children.put(c,newNode );

        return newNode;
    }


    protected Collection<Integer> allPermission() {
        List<Integer> results = new ArrayList<>();
        results.addAll(this.permission);
        for (Map.Entry<Character, UserPermissionTrie> entry : children.entrySet()) {
            UserPermissionTrie child = entry.getValue();
            Collection<Integer> childPrefixes = child.allPermission();
            results.addAll(childPrefixes);
        }
        return results;
    }



}
