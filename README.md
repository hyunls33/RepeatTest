# Retrofit 및 RxJava 사용

Retrofit과 RxJava를 사용한 Android 프로젝트.


# 사용 라이브러리
## Retrofit
![retrofit](https://user-images.githubusercontent.com/36907266/74799161-1e88fe80-5313-11ea-9b66-3d2ac0cd1a8a.png)
 - Square사에서 제공하는 Http 통신을 위한 오픈소스 라이브러리
 - REST API를 안드로이드에서 쉽게 이용할 수 있게 해주는 도구
 - 호출 및 에러처리 간편

## RxJava *(Reactive Extensions Java)*
![rxjava](https://user-images.githubusercontent.com/36907266/74799226-4ed09d00-5313-11ea-92f1-f9b83faf5a28.png)
 - 객체지향형인  Java를 Reactive Programming이 가능하게 해주는 구현체
 - Reactive  Programming : 데이터나 이벤트 변화와 반응에 초점을 맞춘 프로그래밍
 - Java가 변화에 반응할 수 있도록 도와주는 라이브러리
 - Observer pattern을 응용, 확장한 라이브러리
 
	> **Observer pattern**
	> 객체의 상태 변화를 관찰하는 관찰자들, 즉 옵저버들의 목록을 객체에 등록하여 상태 변화가 있을 때마다 메서드 등을 통해 객체가 직접 목록의 각 옵저버에게 통지하도록 하는 디자인 패턴
	
 - 비동기 처리  및 Callback 처리를 쉽게 다룰 수 있음

	 > 설명 참조
	 > https://zeddios.tistory.com/689
	 > http://reactivex.io/documentation/ko/observable.html


# 개선사항

 1. Retrofit에서 RxJava 연결 설정

	> Retrofit 사용 시, 통신 후 RxJava에서 사용하는 Observables객체를 리턴할 수 있도록 adapter 등록이 필요하다.
	
 2. Retrofit Interceptor 사용

	> API 호출 시 토큰 인증방식을 사용하기 위해서는 각 API마다 Header에 토큰을 추가해서 사용해야 했으나, Interceptor 사용 시 모든 API Header에 일괄적으로 토큰을 넣어줄 수 있다.
	
 3. RxJava를 이용한 API 반복 호출

	> 기존 페이지네이션과 같은 API 반복 호출 시 매번 호출을 위한 작업을 하고 있었으나, RxJava에서 지원해주는 함수를 사용하면 간편한 코드로 API를 반복해서 호출할 수 있다.
	
 4. 메모리 낭비

	> RxJava는 Observable을 사용하여 데이터 변화를 감지한다. 따라서 더이상 사용하지 않을 경우, 사용하지 않는 Observable의 구독 해지를 해주어야 리소스 및 메모리 낭비가 없다. (CompositeDisposable 이용)
	
 5. Dialog의 ClickEvent 처리

	> 기존 Dialog 사용 시 PositiveButton과 NegativeButton을 클릭했을 때의 이벤트 처리가 필요할 경우에는 매번 신규로 생성해서 사용했으나, EventListener를 설정해주면 Dialog 공용화가 가능하다.

 6. String

	> 언어 지원 시 strings.xml을 사용하면 여러 언어를 간편하게 지원해줄 수 있다. 해당 파일에서는 매개변수도 사용이 가능하기 때문에 변수 처리도 가능하다.

 7. 앱 축소, 최적화 및 난독화 설정

	> build.gradle에서 설정을 통해 미사용 코드와 리소스를 삭제할 수 있다. 축소를 사용하면 앱 클래스와 멤버의 이름을 줄이는 난독화 및 앱 크기를 추가로 줄이는 더 공격적인 전략을 적용하는 최적화 기능을 활용할 수도 있다. (DTO는 예외처리 필요)
