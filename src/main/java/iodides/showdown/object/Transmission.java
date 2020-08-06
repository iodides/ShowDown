package iodides.showdown.object;

import java.util.ArrayList;

public class Transmission {

    private String name;
    private String hash;
    private String magnet;
    private Long status;
    private String statusName;
    private int percent;
    private boolean finish;
    ArrayList<String> files = new ArrayList<String>();

    @Override
    public String toString() {
      return "name:"+ name +" hash:"+ hash +" status:"+statusName +" percent:"+percent +" finish:"+finish;
    }

    //getter
	  public String getHash() {
		  return hash;
    }
    public String getName() {
		  return name;
    }
    public String getMagnet() {
      return magnet;
    }
    public Long getStatus() {
		  return status;
	  }
    public String getStatusName() {
		  return statusName;
    }
    public int getPercent() {
      return percent;
    }
    public boolean isFinish() {
      return finish;
    }
    public int getFileSize() {
      return files.size();
    }
    public ArrayList<String> getFiles() {
      return files;
    }

    //setter
    public void setHash(String hash) {
		  this.hash = hash;
    }
    public void setName(String name) {
		  this.name = name;
    }
    public void setMagnet(String magnet) {
      this.magnet = magnet;
    }
    public void setStatus(Long status) {
      this.status = status;
      if(status==0) {
        statusName = "멈춤";
      } else if (status==4) {
        statusName = "다운로드중";
      } else if (status==6) {
        statusName = "시딩중";
      } else if (status==3) {
        statusName = "대기중";
      }
      setFinish();
	  }
    public void setPercent(int percent) {
      this.percent = percent;
      setFinish();
    }
    private void setFinish() {
      if(status==0 && percent==100) {
        finish = true;
      }else {
        finish = false;
      }
    }

    public void setFile(String fileName) {
      files.add(fileName);
    }
}