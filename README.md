# Mono-to-Multi
모놀리식 프로젝트를 멀티모듈, 모노레포로 변환하는 과정이 담겨있는 예시 프로젝트입니다.

</br>
</br>

# 실행방법

## 1. 도커 이미지 빌드
```
 docker buildx build --platform linux/amd64 -f order-service/Dockerfile -t order-service:latest . --load
 docker buildx build --platform linux/amd64 -f eureka-server/Dockerfile -t eureka-server:latest . --load
 docker buildx build --platform linux/amd64 -f gateway-service/Dockerfile -t gateway-service:latest . --load
 docker buildx build --platform linux/amd64 -f product-service/Dockerfile -t product-service:latest . --load
 docker buildx build --platform linux/amd64 -f user-service/Dockerfile -t user-service:latest . --load
```

## 2. compose up
`docker compose up`
