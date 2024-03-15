package com.example.degree.Request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RenameDocumentRequest {

    private Integer documentId;

    private String newFilename;
}
