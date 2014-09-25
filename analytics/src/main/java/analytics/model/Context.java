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
 * Context is a dictionary of extra, free-form information about a specific API call. You can add
 * any custom data to the context dictionary that you'd like to have access to in the raw logs.
 * <p/>
 * Some keys in the context dictionary have semantic meaning and will be collected for you
 * automatically, depending on the library you send data from.Some keys need to be manually
 * entered, such as IP Address, speed, etc.
 */
public class Context extends Entity {
  public static class App {
    public String name;
    public String version;
    public String build;

    public App(String name, String version, String build) {
      this.name = name;
      this.version = version;
      this.build = build;
    }
  }

  public static class Campaign {
    public String name;
    public String source;
    public String medium;
    public String term;
    public String content;

    Campaign(String name, String source, String medium, String term, String content) {
      this.name = name;
      this.source = source;
      this.medium = medium;
      this.term = term;
      this.content = content;
    }
  }

  public static class Device {
    public String id;
    public String manufacturer;
    public String model;
    public String name;
    public String type;
    public String brand;

    public Device(String id, String manufacturer, String model, String name, String type,
        String brand) {
      this.id = id;
      this.manufacturer = manufacturer;
      this.model = model;
      this.name = name;
      this.type = type;
      this.brand = brand;
    }
  }

  public static class Library {
    public String name;
    public int version;

    public Library(String name, int version) {
      this.name = name;
      this.version = version;
    }
  }

  public static class Location {
    public double latitude;
    public double longitude;
    public double speed;

    Location(double latitude, double longitude, double speed) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.speed = speed;
    }
  }

  public static class Network {
    public boolean bluetooth;
    public String carrier;
    public boolean cellular;
    public boolean wifi;

    public Network(boolean bluetooth, String carrier, boolean cellular, boolean wifi) {
      this.bluetooth = bluetooth;
      this.carrier = carrier;
      this.cellular = cellular;
      this.wifi = wifi;
    }
  }

  public static class OS {
    public String name;
    public String version;

    public OS(String name, String version) {
      this.name = name;
      this.version = version;
    }
  }

  public static class Referrer {
    public String id;
    public String link;
    public String name;
    public String type;
    public String url;

    Referrer(String id, String link, String name, String type, String url) {
      this.id = id;
      this.link = link;
      this.name = name;
      this.type = type;
      this.url = url;
    }
  }

  public static class Screen {
    public float density;
    public int height;
    public int width;

    public Screen(float density, int height, int width) {
      this.density = density;
      this.height = height;
      this.width = width;
    }
  }

  App app;
  Campaign campaign;
  Device device;
  String ip;
  Library library;
  String locale;
  Location location;
  Network network;
  OS os;
  Referrer referrer;
  Screen screen;
  String groupId;
  String userAgent;

  public Context setApp(App app) {
    this.app = app;
    return this;
  }

  public Context setCampaign(Campaign campaign) {
    this.campaign = campaign;
    return this;
  }

  public Context setDevice(Device device) {
    this.device = device;
    return this;
  }

  public Context setIp(String ip) {
    this.ip = ip;
    return this;
  }

  public Context setLibrary(Library library) {
    this.library = library;
    return this;
  }

  public Context setLocale(String locale) {
    this.locale = locale;
    return this;
  }

  public Context setLocation(Location location) {
    this.location = location;
    return this;
  }

  public Context setNetwork(Network network) {
    this.network = network;
    return this;
  }

  public Context setOs(OS os) {
    this.os = os;
    return this;
  }

  public Context setReferrer(Referrer referrer) {
    this.referrer = referrer;
    return this;
  }

  public Context setScreen(Screen screen) {
    this.screen = screen;
    return this;
  }

  public Context setGroupId(String groupId) {
    this.groupId = groupId;
    return this;
  }

  public Context setUserAgent(String userAgent) {
    this.userAgent = userAgent;
    return this;
  }
}