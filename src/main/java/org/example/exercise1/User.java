package org.example.exercise1;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private int id;
    private String name;
    private String username;
    private String email;
    private Address address;
    private String phone;
    private String website;
    private Company company;
}
@Data
@Builder
    class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;
    }
@Data
@Builder
class Geo {
    private String lat;
    private String lng;
}
@Data
@Builder
class Company {
    private String name;
    private String catchPhrase;
    private String bs;


}
