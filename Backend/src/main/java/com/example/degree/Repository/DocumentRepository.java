package com.example.degree.Repository;

import com.example.degree.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Boolean existsByAccount_IdAndDocName(Integer account_id, String docName);

    Boolean existsByAccount_IdAndId(Integer account_id, Integer id);

    List<Document> getAllByAccount_Id(Integer accountId);

    Document getById(Integer documentId);

    boolean existsById(Integer document_id);
}
