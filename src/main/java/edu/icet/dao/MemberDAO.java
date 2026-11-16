package edu.icet.dao;

import edu.icet.db.DBConnection;
import edu.icet.model.Member;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class MemberDAO {

    Session session = DBConnection.getSession();

    public int getTotalMembers() {
        Long totalMembersResult = session
                .createQuery("SELECT COUNT(m) FROM Member m", Long.class)
                .getSingleResult();
        return totalMembersResult.intValue();
    }

    public List<Member> getAllMembers() {
        return session
                .createQuery("FROM Member", Member.class)
                .getResultList();
    }

    public boolean saveMember(Member member) {
        try {
            Transaction transaction = session.beginTransaction();
            session.persist(member);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateMember(String memberId, Member member) {
        Transaction transaction = session.beginTransaction();
        Member existingMember = session.find(Member.class, memberId);
        if (existingMember == null) {
            return false;
        }
        existingMember.setMemberId(member.getMemberId());
        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setPhone(member.getPhone());
        existingMember.setAddress(member.getAddress());
        transaction.commit();
        return true;
    }

    public boolean deleteMember(String memberId) {
        try {
            Transaction transaction = session.beginTransaction();
            Member member = session.find(Member.class, memberId);
            if (member == null) {
                return false;
            }
            session.remove(member);
            transaction.commit();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
