package ma.nourlab.earthquakealarm.infrastructure.repository;

import ma.nourlab.earthquakealarm.domain.MessageInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageInfo, Long> {
}