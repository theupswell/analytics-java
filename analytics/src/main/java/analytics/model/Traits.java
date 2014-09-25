/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Segment.io, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package analytics.model;

/**
 * Traits can be anything you want, but some of them have semantic meaning and we treat them in
 * special ways. For example, whenever we see an email trait, we expect it to be the user's email
 * address. And we'll send this on to integrations that need an email, like Mailchimp. For that
 * reason, you should only use special traits for their intended purpose.
 */
public class Traits extends Entity {
  public static class Address {
    public String city;
    public String country;
    public String postalCode;
    public String state;
    public String street;

    public Address(String city, String country, String postalCode, String state, String street) {
      this.city = city;
      this.country = country;
      this.postalCode = postalCode;
      this.state = state;
      this.street = street;
    }
  }

  public Address address;
  public String avatar;
  public String createdAt;
  public String description;
  public String email;
  public String fax;
  public String id;
  public String name;
  public String phone;
  public String website;

  public void setAddress(Address address) {
    this.address = address;
  }

  public void setAvatar(String avatar) {
    this.avatar = avatar;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setWebsite(String website) {
    this.website = website;
  }
}