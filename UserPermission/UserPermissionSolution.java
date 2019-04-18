
import com.google.common.collect.ImmutableSet;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class UserPermissionSolution {
    private final Map<Character,UserPermissionTrie> userMap = new HashMap<>();
    public Map<Character, UserPermissionTrie> getUserMap() {
        return userMap;
    }

    public UserPermissionTrie insert(Character userId, Character parentId, Set<Integer> permission){

        UserPermissionTrie found =  userMap.get(parentId);
        if(Objects.nonNull(found)){
           found = found.addChild(userId,permission);
        } else {
            //day chinh la root
            found = new UserPermissionTrie(userId,permission);
        }
        userMap.put(userId,found);
        return found;
    }

    public static void main(String ...s){
        UserPermissionSolution permissionSolution = new UserPermissionSolution();
        UserPermissionTrie ceo =  permissionSolution.insert('A',null,ImmutableSet.of(1));
        UserPermissionTrie userB =  permissionSolution.insert('B',ceo.id,ImmutableSet.of(2,3));
        UserPermissionTrie userC =  permissionSolution.insert('C',ceo.id,ImmutableSet.of(4,5));

        UserPermissionTrie userD =  permissionSolution.insert('D',userB.id,ImmutableSet.of(8));
        UserPermissionTrie userE =  permissionSolution.insert('E',userC.id,ImmutableSet.of(10));

        UserPermissionTrie userA =  permissionSolution.getUserMap().get('A');
        System.out.println(org.apache.commons.lang3.StringUtils.join(userA.allPermission(),","));
        System.out.println(org.apache.commons.lang3.StringUtils.join(userC.allPermission(),","));
        System.out.println(org.apache.commons.lang3.StringUtils.join(userE.allPermission(),","));

        System.out.println(org.apache.commons.lang3.StringUtils.join(permissionSolution.getUserMap().get('B').allPermission(),","));
        System.out.println(org.apache.commons.lang3.StringUtils.join(permissionSolution.getUserMap().get('D').allPermission(),","));
    }

}
