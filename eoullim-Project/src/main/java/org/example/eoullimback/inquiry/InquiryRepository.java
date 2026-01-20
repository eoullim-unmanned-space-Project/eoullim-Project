package org.example.eoullimback.inquiry;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InquiryRepository extends CrudRepository<InquiryChat, Long> {

    List<InquiryChat> findAllBySenderAndReceiverOrReceiverAndSender(
            String sender, String receiver, String sender1, String receiver1);

    List<InquiryChat> findAllByRoomOrderByCreatedAtAsc(InquiryChatRoom room);
}
