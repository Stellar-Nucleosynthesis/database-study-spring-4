package org.example.practice5;

import org.example.practice5.config.DataSourceContextHolder;
import org.example.practice5.entities.User;
import org.example.practice5.service.UserService;
import org.example.practice5.util.DataSourceEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ClusterReplicationTest extends AbstractPostgresTest{
    @Autowired
    private UserService userService;

    @Autowired
    private DataSourceContextHolder contextHolder;

    @Test
    void writeOnPrimary_readOnReplica_shouldBeSynchronized() {
        contextHolder.setBranchContext(DataSourceEnum.PRIMARY);
        User user = new User();
        user.setName("replication-test");
        User saved = userService.add(user);
        Integer id = saved.getId();

        Optional<User> fromReplica = userService.findById(id);
        assertThat(fromReplica).isPresent();
    }
}