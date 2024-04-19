FROM node:18-alpine 
WORKDIR /auto-pass-app/
COPY . /

RUN npm install --only=production
RUN npm install typescript

CMD ["npm", "run", "start"]

