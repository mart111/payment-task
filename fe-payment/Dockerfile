FROM node:20-alpine

RUN npm install -g serve

WORKDIR /app

COPY package.json /app

RUN npm install

COPY . .

RUN npm run build

EXPOSE 3000

ENTRYPOINT ["serve","-s", "build"]