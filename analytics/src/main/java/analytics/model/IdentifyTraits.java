package analytics.model;

import java.util.Date;

public class IdentifyTraits extends Traits {
  short age;
  Date birthday;
  String firstName;
  String gender;
  String lastName;
  String title;
  String username;

  public short getAge() {
    return age;
  }

  public Date getBirthday() {
    return birthday;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getGender() {
    return gender;
  }

  public String getLastName() {
    return lastName;
  }

  public String getTitle() {
    return title;
  }

  public String getUsername() {
    return username;
  }
}
