package com.yusuf.lims.service;

import com.yusuf.lims.dto.WriterDto;
import com.yusuf.lims.entity.Writer;

public interface WriterService {

    void saveWriter(WriterDto writerDto);

    Writer findByWriterName(String name);
}
