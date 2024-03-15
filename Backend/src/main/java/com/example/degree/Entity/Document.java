package com.example.degree.Entity;


import jakarta.persistence.*;
import lombok.*;

@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "documents")
@Entity
public class Document {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String docName;

    private String docType;

    @Column(columnDefinition = "MEDIUMBLOB  ")
    private byte[] data;

    @OneToOne(cascade = CascadeType.ALL)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JoinColumn(name = "account_id",  referencedColumnName = "id")
    private Account account;

    public Document(String docName, String docType, byte[] data, Account account) {
        this.docName = docName;
        this.docType = docType;
        this.data = data;
        this.account = account;
    }
}
