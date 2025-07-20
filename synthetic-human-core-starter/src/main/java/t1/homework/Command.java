package t1.homework;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Command {

    @NotBlank(message = "Описание не может быть пустым")
    @Size(max = 1000, message = "Описание не может быть длиннее 1000 символов")
    private String description;

    @NotNull(message = "Укажите приоритет команды")
    private CommandPriority commandPriority;

    @NotBlank(message = "Укажите автора")
    @Size(max = 100, message = "Автор не может быть длиннее 100 символов")
    private String author;


    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}(:\\d{2})?(\\.\\d+)?(Z|[+-]\\d{2}:\\d{2})$", message = "Неверный формат времени, используйте ISO 8601(пример: 2025-07-20T08:30:00Z или 2025-07-20T08:30:00+03:00)")    
    @NotBlank(message = "Укажите время")
    private String time;

}


