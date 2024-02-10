package com.example.demo.payload.response;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String fName;
  private String lName;
  private String username;
  private String email;
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String fName,String lName,String username, String email, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.fName = fName;
    this.lName = lName;
    this.username = username;
    this.email = email;
    this.roles = roles;
  }


  public JwtResponse(String jwt, String username, Set<String> roles) {
    this.token = jwt;
    this.username = username;
    this.roles = (List<String>) roles;
  }
}
