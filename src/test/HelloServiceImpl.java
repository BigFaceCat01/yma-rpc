import com.yma.rpc.core.provider.annotation.Rpc;

/**
 * @author Created by huang xiao bao
 * @date 2019-05-14 17:25:20
 */
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String s) {
        return s;
    }
}
