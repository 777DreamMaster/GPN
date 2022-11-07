package club.gpn.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ApiRequest {
    String user_id;
    String group_id;
}
