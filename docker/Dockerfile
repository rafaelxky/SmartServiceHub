
FROM archlinux:latest

ENV LANG=C.UTF-8
ENV LC_ALL=C.UTF-8
ENV TZ=UTC
ENV POSTGRES_DATA=/var/lib/postgres/data

WORKDIR /SmartServiceApp

RUN pacman -Sy --noconfirm pacman-contrib curl && \
    curl -o /etc/pacman.d/mirrorlist https://archlinux.org/mirrorlist/all/ && \
    sed -i 's/^#Server/Server/' /etc/pacman.d/mirrorlist && \
    rankmirrors -n 5 /etc/pacman.d/mirrorlist > /etc/pacman.d/mirrorlist.tmp && \
    mv /etc/pacman.d/mirrorlist.tmp /etc/pacman.d/mirrorlist


RUN pacman-key --init && pacman-key --populate archlinux

RUN pacman -Syu --noconfirm \
    base-devel git vim curl \
    postgresql jdk-openjdk maven \
    nodejs npm \
    nginx lua tor supervisor && \
    pacman -Scc --noconfirm

RUN mkdir -p $POSTGRES_DATA && \
    chown -R postgres:postgres $POSTGRES_DATA

USER postgres
RUN initdb -D $POSTGRES_DATA
USER root

COPY . .

RUN chmod +x ./scripts/*.sh

RUN echo "[supervisord]\nnodaemon=true\n\n\
[program:postgres]\ncommand=/usr/bin/postgres -D $POSTGRES_DATA\nuser=postgres\nautorestart=true\n\n\
[program:tor]\ncommand=/usr/bin/tor\nautorestart=true\n\n\
[program:nginx]\ncommand=/usr/bin/nginx -g 'daemon off;'\nautorestart=true\n\n\
[program:backend]\ncommand=./scripts/start_backend.sh\nautorestart=true\n\n\
[program:frontend]\ncommand=./scripts/start_frontend.sh\nautorestart=true" \
> /etc/supervisord.conf

EXPOSE 8080 5173 80 9050 9051

HEALTHCHECK --interval=30s --timeout=5s CMD curl -f http://localhost:8080 || exit 1

CMD ["/usr/bin/supervisord", "-c", "/etc/supervisord.conf"]