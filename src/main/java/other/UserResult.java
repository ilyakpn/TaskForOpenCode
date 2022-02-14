package other;

import com.test.taskopencode.model.Answer;
import com.test.taskopencode.model.Question;
import com.test.taskopencode.model.User;
import lombok.Data;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class UserResult {

    private User user;

    private Map<Question, List<Answer>> results = new LinkedHashMap<>();

}
