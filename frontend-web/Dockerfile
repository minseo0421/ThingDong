FROM node:alpine as builder

WORKDIR /usr/src/app
COPY ./package.json /usr/src/app/package.json
RUN npm install --force

COPY . /usr/src/app
RUN npm run build

FROM nginx
EXPOSE 3000
COPY ./nginx/default.conf /etc/nginx/conf.d/default.conf

COPY --from=builder /usr/src/app/dist /usr/share/nginx/html
