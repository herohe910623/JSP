package me.herohe.commonweb.account;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service    //빈의 이름은 기본적으로 accountAuditAware 가 된다.
public class AccountAuditAware implements AuditorAware<Account> {
    @Override
    public Optional<Account> getCurrentAuditor() {
        System.out.println("Looking for current User");
        return Optional.empty();
    }
}
