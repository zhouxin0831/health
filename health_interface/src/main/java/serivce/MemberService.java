package serivce;

import com.itheima.pojo.Member;
import org.apache.poi.ss.formula.functions.Count;

import java.util.List;
import java.util.Map;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);


    //List<Integer> findMemberCountByMonth(List<String> list);

    Map findMember();
}
