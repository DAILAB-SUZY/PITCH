FROM node:23-alpine AS frontend-builder
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:latest

# NGINX 설정 파일 복사
COPY ./nginx/default.conf /etc/nginx/conf.d/
COPY ./nginx/ubiquiti_fullchain.crt /etc/ssl/certs/ubiquiti_fullchain.crt
COPY ./nginx/ubiquiti_key.pem /etc/ssl/private/ubiquiti_key.pem
COPY --from=frontend-builder /app/dist/ /usr/share/nginx/html/