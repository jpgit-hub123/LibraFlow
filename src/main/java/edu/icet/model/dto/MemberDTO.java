package edu.icet.model.dto;

import edu.icet.db.DBConnection;
import edu.icet.model.entity.BookIssueEntity;
import edu.icet.model.entity.MemberEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MemberDTO {

    Session session = DBConnection.getSession();

    public int getTotalMembers() {
        Long totalMembersResult = session
                .createQuery("SELECT COUNT(m) FROM MemberEntity m", Long.class)
                .getSingleResult();
        return totalMembersResult.intValue();
    }

    public List<MemberEntity> getAllMembers() {
        return session
                .createQuery("FROM MemberEntity", MemberEntity.class)
                .getResultList();
    }

    public boolean saveMember(MemberEntity memberEntity) {
        try {
            Transaction transaction = session.beginTransaction();
            session.persist(memberEntity);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateMember(String memberId, MemberEntity memberEntity) {
        Transaction transaction = session.beginTransaction();
        MemberEntity existingMemberEntity = session.find(MemberEntity.class, memberId);
        if (existingMemberEntity == null) {
            return false;
        }
        existingMemberEntity.setName(memberEntity.getName());
        existingMemberEntity.setEmail(memberEntity.getEmail());
        existingMemberEntity.setPhone(memberEntity.getPhone());
        existingMemberEntity.setAddress(memberEntity.getAddress());
        transaction.commit();
        return true;
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

    public boolean hasIssueHistory(int memberId) {
        return !session.createQuery("FROM BookIssueEntity bi WHERE bi.memberId = :memberId", BookIssueEntity.class)
                .setParameter("memberId", memberId)
                .setMaxResults(1)
                .getResultList()
                .isEmpty();
    }
}
