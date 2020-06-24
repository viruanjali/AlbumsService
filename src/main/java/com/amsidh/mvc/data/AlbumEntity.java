
package com.amsidh.mvc.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AlbumEntity {
    private long id;
    private String albumId;
    private String userId; 
    private String name;
    private String description; 
}
