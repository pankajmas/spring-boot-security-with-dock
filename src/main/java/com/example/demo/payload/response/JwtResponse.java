package com.example.demo.payload.response;

import java.util.List;

import org.joda.time.LocalDateTime;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String fName;
  private String lName;
  private String username;
  private String email;
  private LocalDateTime localDateTime;
  private List<String> roles;

  public JwtResponse(String accessToken, Long id, String fName,String lName,String username, String email,LocalDateTime time, List<String> roles) {
    this.token = accessToken;
    this.id = id;
    this.fName = fName;
    this.lName = lName;
    this.username = username;
    this.localDateTime = time;
    this.email = email;
    this.roles = roles;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public List<String> getRoles() {
    return roles;
  }

public String getfName() {
	return fName;
}

public void setfName(String fName) {
	this.fName = fName;
}

public String getlName() {
	return lName;
}

public void setlName(String lName) {
	this.lName = lName;
}

public LocalDateTime getLocalDateTime() {
	return localDateTime;
}

public void setLocalDateTime(LocalDateTime localDateTime) {
	this.localDateTime = localDateTime;
}
  
}
