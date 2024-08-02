package com.task05;


import lombok.*;

import java.util.Map;
import java.util.UUID;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Event {
   private UUID id;
   private int principalId;
   private String createdAt;
   private Map<String,String> body;

}
