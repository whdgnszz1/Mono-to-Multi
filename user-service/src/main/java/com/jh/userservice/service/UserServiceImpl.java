package com.jh.userservice.service;

import com.jh.common.exception.BizRuntimeException;
import com.jh.userservice.dto.LoginRequestDto;
import com.jh.userservice.dto.SignupRequestDto;
import com.jh.userservice.entity.User;
import com.jh.userservice.repository.UserRepository;
import com.jh.userservice.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User signup(SignupRequestDto createUserRequestDto) {
        try {
            if (userRepository.findByEmail(createUserRequestDto.getEmail()).isPresent()) {
                throw new BizRuntimeException("이미 사용중인 이메일입니다.");
            }

            String password = createUserRequestDto.getPassword();
            if (!isPasswordValid(password)) {
                throw new BizRuntimeException("정책에 어긋난 비밀번호입니다.");
            }

            User user = new User();
            user.setEmail(createUserRequestDto.getEmail());
            user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));

            return saveUser(user);
        } catch (DataAccessException e) {
            log.error("회원가입 처리 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("회원가입 처리 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (BizRuntimeException e) {
            log.error("회원가입 처리 중 비즈니스 로직 오류 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("회원가입 처리 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("회원가입 처리 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    @Override
    public String login(LoginRequestDto loginRequestDto) {
        try {
            Optional<User> user = userRepository.findByEmail(loginRequestDto.getEmail());
            if (user.isEmpty()) {
                throw new BizRuntimeException("존재하지 않는 유저입니다.");
            }

            if (passwordEncoder.matches(loginRequestDto.getPassword(), user.get().getPassword())) {
                return jwtUtil.createToken(user.get().getUserId(), user.get().getRole().toString());
            } else {
                throw new BizRuntimeException("잘못된 비밀번호입니다.");
            }
        } catch (DataAccessException e) {
            log.error("로그인 처리 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("로그인 처리 중 데이터베이스 오류가 발생했습니다.", e);
        } catch (BizRuntimeException e) {
            log.error("로그인 처리 중 비즈니스 로직 오류 발생", e);
            throw e;
        } catch (Exception e) {
            log.error("로그인 처리 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("로그인 처리 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*?_])[A-Za-z\\d!@#$%^&*?_]{8,16}$";
        return password.matches(passwordPattern);
    }

    private User saveUser(User user) {
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("회원 저장 중 데이터베이스 오류 발생", e);
            throw new BizRuntimeException("회원 저장에 실패했습니다.", e);
        } catch (Exception e) {
            log.error("회원 저장 중 예기치 않은 오류 발생", e);
            throw new BizRuntimeException("회원 저장 중 예기치 않은 오류가 발생했습니다.", e);
        }
    }
}
