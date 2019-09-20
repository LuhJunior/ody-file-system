package ody_file_system_server;

public class Request {
  long lastRequest;
  int count;

  public Request(long lastRequest, int count) {
    this.lastRequest = lastRequest;
    this.count = count;
  }

  public long getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(long lastRequest) {
    this.lastRequest = lastRequest;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

}
