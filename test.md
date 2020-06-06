# CentOS 7.8 

## 설치

- 설치 이미지 : CentOS-7-x86_64-DVD-2003.iso
- 다운로드 페이지 : [https://wiki.centos.org/Download](https://wiki.centos.org/Download)

- 이미지 넣고 부팅후 `Install CentOS 7` 선택

- 언어 설정에서 `한국어` 선택

### 설치 요약 화면

- 소트프웨어 선택은 `최소 설치`
- 설치 대상에서 `파티션 설정`
- KDUMP 에서 `KDUMP 활성화 체크해제`

### 네트워크 & 호스트 이름

- 네트워크 & 호스트 이름 에서 `호스트 이름 설정`, 
- 설정 > 일반 > `사용 가능하면 자동으로 이 네트워크에 연결 체크`
- 설정 > IPv4 설정 에서 `수동` 으로 변경후 `IP 주소 입력`
- DNS 주소 입력
- 이더넷 연결 상태 확인

### 설치 시작

- root 암호 설정
- 설치 완료후 재부팅

## 설치후 작업

- 네트워크 확인
```bash
@ vi /etc/sysconfig/network-script/ifcfg-ens32
```

- CROM 마운드
```bash
@ mkdir /mnt/cdrom
@ mount /dev/cdrom /mnt/cdrom
mount: /dev/sr0 is write-protected, mounting read-only
```

- net-tools 설치
```bash
@ cd /mnt/cdrom/Packages
@ rpm -Uvh net-tools-2.0-0.25.20131004git.el7.x86_64.rpm
```
- CentOS 패키지 업데이트
```
@ yum update 
```

테스트

    테스트
ㅁㄴㅇㅁㄴㅇ

    cd /mnt/cdrom/Packages
    @ rpm 
kkkl
*xptmxm*
***

















