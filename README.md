소중한 오픈 소스 활용SW 경진대회 
# 국립한밭대학교 Little Sentinels팀

## 주제 
- 딥러닝 기반 딸기 숙성도 분류 및 생성형 AI 를 활용한 다국어 음성 지원 시스템 
 
  
## 팀 구성 
- 20202066 안다은
- 20201737 백민우
- 20222008 조민서
- 20222278 박동현

## Project Background
- ### 1. 개요
  우리나라의 농업 인구 고령화로 인해 외국인 노동자의 비율이 증가하고 있다. 농업 분야에서 외국인 노동자가 차지하는 비중이 높아짐에 따라, 언어 장벽이 생산성과 작업 효율성에 부정적인 영향을 미치고 있다.
  본 프로젝트는 생성형 AI를 활용한 다국어 음성 인터페이스를 제공하여, 로봇 수확 시스템을 보다 쉽게 조작하고 효율적으로 운영할 수 있도록 지원하는 것을 목표로 한다.
  우리는 AI 기반 다국어 스마트 농업 도우미 시스템을 개발하고, 딸기 수확 현장에서 실시간 피드백과 작업 안내를 제공하는 솔루션을 구축한다.

- ### 2. 문제 정의 및 필요성
  - 외국인 근로자의 언어 장벽으로 인한 **작업 효율 저하**
  - 농장주-작업자 간 **의사소통 부족**으로 인한 품질 하락
  - 경험 중심 판단으로 **숙성도 판단 오류** 및 **품질 편차 발생**

- ###  3. 해결 방안
  - 생성형 AI 기반 **다국어 음성 안내 시스템** 도입
  - 문화 등을 반영한 자연스로운 메시지 번역 지원
  - HSV 색상 기반 **정량적 성숙도 분석**

    
## 프로젝트 내용
  - ### 구현 내용
    - 딸기 이미지 데이터 셋을 구축
    - 딸기 재배 구역별 RGB-D 카메라로 딸기 이미지 촬영
    - 딸기 촬영 데이터로 딸기의 Bounding Box 및 Instance Segmentation 생성
    - 딸기를 픽셀 단위로 분할 후 딸기의 색상(Hue, Saturation, Value)을 분석하여 숙성도를 측정
    - 숙성도 및 구역별 통계 자료를 데이터베이스에 전송 
    - 데이터를 생성형 AI로 처리 및 다국어 음성 및 텍스트로 변형하여 사용자에게 제공
    - 문화 등을 반영한 자연스로운 메시지 번역 및 메시지 전송 연동
      
  - ### 기대 효과
    - 외국인 근로자 언어 장벽 해소
      - 현장 커뮤니케이션 원활화 
      - 작업자 만족도 및 조직 신뢰 강화
    - 품질 균일화 및 비용 절감
      - 과실 선별 효율화로 품질 균일화·폐기율 감소
      - 작업 시간 단축으로 인건비 감소
    - AI 분류 정확도 향상 · 범용성 강화
      - 딥러닝 기반 성숙도 분류 정확도 개선
      - 다국어 음성지원 AI 기술로 범용성 강화

## 개발환경
- ### DBMS  
  - Firebase Firestore
- ### 개발 언어  
  - Android 앱: Java 11,
  - 모델 학습·서빙: Python 3.10
- ### 모델 및 학습 환경  
  - OS: Ubuntu 22.04 LTS   
  - CUDA: 11.8  
  - cuDNN: 8.6  
  - Python 패키지:  
    - PyTorch 2.0.1 (CUDA 11.8)  
    - torchvision 0.15.2  
    - OpenCV 4.8.0  
- ### 프레임워크/라이브러리  
  - ML 추론(Android): PyTorch Mobile 2.0.1  
  - Android UI: Android SDK
- ### 개발 도구 및 서비스  
  - IDE: Android Studio Meerkat  
  - 빌드 툴: Gradle 7.4  
  - 버전 관리: Git/GitHub
- ### 서버 대여
  - 플랫폼: Vast.ai
