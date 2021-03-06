# ORM 1+N problem cases

JPA를 이용할 때 발생하는 문제 중 가장 쉽게 접할 수 있는 N+1 문제에 대해 학습합니다.

## 엔티티 구성
고객과 계좌의 관계로 구성하겠다
- 고객은 여러개의 계좌를 가지고 있다.
- 계좌는 한 명의 고객에만 할당될 수 있다.
- 계좌는 여러 종류로 함

## n+1 문제란?
N+1(1+N) 문제는 ORM을 사용하면 가장 쉽게 접할 수 있는 문제 중에 하나이다.   
1번 쿼리를 날렸는데 추가로 N번 더 쿼리문을 날려야 하는 상황을 1+N 이라고 불린다.

## 발생 상황
- ### case 1 고객의 계좌 쪽 FetchType이 LAZY 방식일 때
  - 계좌에 접근하지 않으면 쿼리가 발생하지 않는다.   
  - 고객 entity를 통해서 계좌에 접근하면 접근하면 쿼리를 날리게 된다.
- ### case 2 고객의 계좌 쪽 FetchType이 EAGER 방식일 때
  - 고객 entity에 접근하는 경우 무조건 계좌정보를 가져오기때문에 n+1 문제가 발생한다.

문제를 재현해서 코드로 작성하려했는데, 아무리해도 n+1 문제가 재현되지않았다.   
문제보단 해결에 집중하도록 하겠다.

## 해결 방법
### 1. fetch join
n+1 자체가 발생하는 이유가 한쪽 테이블만 조회하고 연결된 다른 테이블은 따로 조회하기 때문이다.   
두 테이블을 join하여 한번에 모든 데이터를 가져올 수 있다면 n+1 문제가 발생하지 않을것이다.   
그렇게 나온 해결 방법이 fetchJoin 방법이다.
두 테이블을 join하는 쿼리를 작성하는 것이다.

다음과 같이 JPQL을 직접 지정해준다.   
일대다 조인이므로 DISTINCT를 이용해 중복을 제거하는 것이 좋다.
```java
@Query("select DISTINCT c from Customer c join fetch c.savingsAccount")
    public List<Customer> findAllFetchJoin();
```

결과적으로 한번에 쿼리를 날리는 것을 확인 할 수 있다.   
```
Hibernate: select customer0_.id as id1_0_0_, savingsacc1_.id as id1_1_1_, customer0_.name as name2_0_0_, savingsacc1_.customer_id as customer3_1_1_, savingsacc1_.name as name2_1_1_, savingsacc1_.id as id1_1_0__ from customer customer0_ inner join savings_account savingsacc1_ on customer0_.id=savingsacc1_.id
```

#### 문제점
- 쿼리 한번에 모든 데이터를 다 가져오다보니 페이징 기능을 이용할 수 없다

### 2. BatchSize
BatchSize 엔티티를 이용하면 지정한 사이즈만큼 SQL의 IN 절을 사용해서 조회한다.   
batchSize 만큼 미리 로딩해 뒀다가, 사이즈를 초과할 때 다음 SQL을 실행한다.

```java
    @BatchSize(size = 5)
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SavingsAccount> savingsAccount2 = new ArrayList<>();
```
batchSize마다 쿼리를 날리는 것을 확인할 수 있다.
```
0
Hibernate: select savingsacc0_.id as id1_1_1_, savingsacc0_.id as id1_1_0_, savingsacc0_.customer_id as customer3_1_0_, savingsacc0_.name as name2_1_0_ from savings_account savingsacc0_ where savingsacc0_.id in (?, ?, ?, ?, ?)
1
2
3
4
5
Hibernate: select savingsacc0_.id as id1_1_1_, savingsacc0_.id as id1_1_0_, savingsacc0_.customer_id as customer3_1_0_, savingsacc0_.name as name2_1_0_ from savings_account savingsacc0_ where savingsacc0_.id in (?, ?, ?, ?, ?)
6
7
8
9
```

### 3. SUBSELECT
FetchMode를 SUBSELECT로 사용하면 연관된 데이터를 조회할 때 쿼리를 사용해서 N+1 문제를 해결한다.   
필요할 때 fetch join을 하는 것이라 생각하면 된다.
```java
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "id", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SavingsAccount> savingsAccount3 = new ArrayList<>();
```
```
Hibernate: select customer0_.id as id1_0_, customer0_.name as name2_0_ from customer customer0_
0
Hibernate: select savingsacc0_.id as id1_1_1_, savingsacc0_.id as id1_1_0_, savingsacc0_.customer_id as customer3_1_0_, savingsacc0_.name as name2_1_0_ from savings_account savingsacc0_ where savingsacc0_.id in (select customer0_.id from customer customer0_)
1
2
3
4
5
6
7
8
9
```

## 결론
- 즉시 로딩과 지연 로딩 중 지연 로딩을 사용하는 것을 추천
- 성능 최적화가 필요한 곳에는 JPQL 페치 조인을 사용한다.
- OneToOne, ManyToOne은 즉시 로딩이 기본이므로 지연 로딩 전략으로 변경해서 사용하도록 한다.

## 참고
- https://incheol-jung.gitbook.io/docs/q-and-a/spring/n+1
- JPA 프로그래밍 - 김영한 저