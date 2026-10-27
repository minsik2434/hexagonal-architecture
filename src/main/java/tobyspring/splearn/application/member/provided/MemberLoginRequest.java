package tobyspring.splearn.application.member.provided;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record MemberLoginRequest(
        @Email String email,
        @Size(min = 0, max = 100) String password
) {
}
