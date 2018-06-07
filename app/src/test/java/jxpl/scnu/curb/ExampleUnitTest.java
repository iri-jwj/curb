package jxpl.scnu.curb;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import jxpl.scnu.curb.homePage.immediateInformation.ImmediateInformation;
import jxpl.scnu.curb.utils.autoFitRecycler.BaseData;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        List<BaseData> a = new LinkedList<>();
        ImmediateInformation i = new ImmediateInformation(UUID.randomUUID(), "title", "content",
                "type", "time", "time", "address");
        BaseData a2 = i;
        a.add(a2);
        System.out.println(a2.getStr_id());
        System.out.println(((ImmediateInformation) a.get(0)).getTime());
    }
}