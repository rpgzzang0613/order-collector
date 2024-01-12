# ORDER COLLECTOR API

- JAVA단 소스들
  - com.ammiboroom.ordercollector
    - aspect
      - 예외처리 로직을 분리한 Advice 클래스가 들어있는 패키지
    - controller
      - Controller 클래스를 모아두는 패키지
    - service
      - 비즈니스 로직이 들어가는 Service를 모아두는 패키지
    - dao
      - DB 접근이 이뤄지는 mybatis Mapper를 모아두는 패키지
    - dto
      - 결과를 담아 프론트에 내릴때 사용할 클래스가 들어있는 패키지
    - config
      - Java 코드를 통한 설정 클래스를 모아두는 패키지
    - util
      - 공통적으로 사용되는 유틸리티 클래스를 모아두는 패키지
  - resources/queries
    - mybatis에서 참조할 쿼리 xml을 모아두는 디렉토리
   
   
- Front단 소스들
  - resources/static
    - favicon, svg, img, css, js 등의 정적 파일을 모아두는 디렉토리
  - resources/templates
    - thymeleaf 템플릿 모아두는 디렉토리