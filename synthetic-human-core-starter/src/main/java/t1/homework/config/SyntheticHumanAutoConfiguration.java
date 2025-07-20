package t1.homework.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Configuration
@EnableAspectJAutoProxy
public class SyntheticHumanAutoConfiguration {

    public void init(){
        log.info("конфигурации подгрузилась");
    }
}
