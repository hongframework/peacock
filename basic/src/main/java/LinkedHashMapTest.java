/**
 * Created by zhangquanhong on 2017/11/7.
 */
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
public class LinkedHashMapTest {
    public static void main(String[] args) {

        URI nnUri = URI.create("hdfs://lcsdev-cluster:8020");
        System.out.println(nnUri.getHost());
        System.out.println(nnUri.getAuthority());

        Map map = new FixedSizeLinkedHashMap();
        System.out.println(map.size());
        for(int i = 0; i < 50; i++) {
            map.put(i, true);
            System.out.println(map.size());
            System.out.println(map);
        }
    }
}
class FixedSizeLinkedHashMap extends LinkedHashMap{
    private static final long serialVersionUID = 6918023506928428613L;
    private static int MAX_ENTRIES = 10;
    /**
     * 获得允许存放的最大容量
     * @return int
     */
    public static int getMAX_ENTRIES() {
        return MAX_ENTRIES;
    }
    /**
     * 设置允许存放的最大容量
     * @param int max_entries
     */
    public static void setMAX_ENTRIES(int max_entries) {
        MAX_ENTRIES = max_entries;
    }
    /**
     * 如果Map的尺寸大于设定的最大长度，返回true，再新加入对象时删除最老的对象
     * @param Map.Entry eldest
     * @return int
     */
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_ENTRIES;
    }
}