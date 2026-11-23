package edu.icet.repository.impl;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import edu.icet.repository.MembersRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class MembersRepositoryImpl implements MembersRepository {

    private final Session session = DBConnection.getSession();

    public boolean hasIssueHistory(int memberId) {
        return !session.createQuery("FROM BookIssueEntity bi WHERE bi.memberId = :memberId", BookIssueEntity.class)
                .setParameter("memberId", memberId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }

    public boolean deleteMember(String memberId) {
        try {
            Transaction transaction = session.beginTransaction();
            MemberEntity memberEntity = session.find(MemberEntity.class, memberId);
            if (memberEntity == null) {
                return false;
            }
            session.remove(memberEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<MemberEntity> getAllMembers() {
        return session
                .createQuery("FROM MemberEntity", MemberEntity.class)
                .getResultList();
    }
}
