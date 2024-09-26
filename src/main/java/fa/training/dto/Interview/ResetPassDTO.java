package fa.training.dto.Interview;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResetPassDTO {

    String token;
    String newPass;
    String reNewPass;

}
