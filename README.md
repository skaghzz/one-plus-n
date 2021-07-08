# ORM N+1 problem cases

JPA를 이용할 때 발생하는 문제 중 가장 쉽게 접할 수 있는 N+1 문제에 대해 학습합니다.

## 엔티티 구성
고객과 계좌의 관계로 구성하겠다
- 고객은 여러개의 계좌를 가지고 있다.
- 계좌는 한 명의 고객에만 할당될 수 있다.



## 참고
(https://incheol-jung.gitbook.io/docs/q-and-a/spring/n+1)