ARG BUILD_FROM=ghcr.io/erik73/base:6.0.7
# hadolint ignore=DL3006
FROM ${BUILD_FROM}

# Set shell
SHELL ["/bin/bash", "-o", "pipefail", "-c"]
ARG S6_OVERLAY_VERSION="v3.2.2.0"
ARG ROUNDCUBE_VERSION="1.6.15"

# Build arguments
ARG BUILD_ARCH
ARG BUILD_DATE
ARG BUILD_DESCRIPTION
ARG BUILD_NAME
ARG BUILD_REF
ARG BUILD_REPOSITORY
ARG BUILD_VERSION

# Setup base
RUN apk add --no-cache \
    mariadb-client=11.4.10-r0 \
    nginx=1.28.3-r0 \ 
    php83=8.3.30-r1 \
    php83-fpm=8.3.30-r1 \
    php83-imap=8.3.30-r1 \
    php83-session=8.3.30-r1 \
    php83-mysqli=8.3.30-r1 \
    php83-opcache=8.3.30-r1 \
    php83-mbstring=8.3.30-r1 \
    php83-pdo_mysql=8.3.30-r1 \
    php83-curl=8.3.30-r1 \
    php83-xml=8.3.30-r1 \
    php83-dom=8.3.30-r1 \
    php83-intl=8.3.30-r1 \
    php83-ctype=8.3.30-r1 \
    php83-zip=8.3.30-r1 \
    php83-gd=8.3.30-r1 \
    php83-pecl-imagick=3.8.1-r0 \
    php83-xmlwriter=8.3.30-r1 \
    php83-fileinfo=8.3.30-r1 \
    php83-exif=8.3.30-r1 \
    php83-iconv=8.3.30-r1 \
    && curl -L -s "https://github.com/just-containers/s6-overlay/releases/download/${S6_OVERLAY_VERSION}/syslogd-overlay-noarch.tar.xz" \
        | tar -C / -Jxpf - \
    && sed -i 's#^ + .*$# + -^auth\\. -^authpriv\\. -mail\\. $T ${dir}/everything#' /etc/s6-overlay/s6-rc.d/syslogd-log/run \
    && sed -i 's#^backtick .*$#backtick -D "n20 s1000000 T 1" line { printcontenv S6_LOGGING_SCRIPT }#' /etc/s6-overlay/s6-rc.d/syslogd-log/run \
    && sed -i 's#^s6-socklog .*$#s6-socklog -d3 -U -t3000 -x /run/systemd/journal/dev-log#' /etc/s6-overlay/s6-rc.d/syslogd/run \
    && mkdir /var/www/roundcube \
    && curl -L -s "https://github.com/roundcube/roundcubemail/releases/download/${ROUNDCUBE_VERSION}/roundcubemail-${ROUNDCUBE_VERSION}-complete.tar.gz" \
        | tar zxvf - -C /var/www/roundcube --strip-components=1 \
        && rm -f -r \
        /tmp/* \
        /var/www/roundcube/installer \
    && mkdir -p /run/systemd/journal

# Copy data for app
COPY rootfs /

# Labels
LABEL \
    io.hass.name="${BUILD_NAME}" \
    io.hass.description="${BUILD_DESCRIPTION}" \
    io.hass.arch="${BUILD_ARCH}" \
    io.hass.type="addon" \
    io.hass.version=${BUILD_VERSION} \
    maintainer="Erik Hilton <erikhilton73@gmail.com>" \
    org.opencontainers.image.title="${BUILD_NAME}" \
    org.opencontainers.image.description="${BUILD_DESCRIPTION}" \
    org.opencontainers.image.vendor="Erik73 Hassio-Addons" \
    org.opencontainers.image.authors="Erik Hilton <erikhilton73@gmail.com>" \
    org.opencontainers.image.licenses="MIT" \
    org.opencontainers.image.url="https://github.com/erik73/hassio-addons" \
    org.opencontainers.image.source="https://github.com/${BUILD_REPOSITORY}" \
    org.opencontainers.image.documentation="https://github.com/${BUILD_REPOSITORY}/blob/main/README.md" \
    org.opencontainers.image.created=${BUILD_DATE} \
    org.opencontainers.image.revision=${BUILD_REF} \
    org.opencontainers.image.version=${BUILD_VERSION}
