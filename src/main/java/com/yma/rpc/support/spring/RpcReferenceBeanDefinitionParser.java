package com.yma.rpc.support.spring;

import com.yma.rpc.support.spring.enums.ConfigTypeEnum;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * @author Created by huang xiao bao
 * @date 2019-06-01 14:41:38
 */
public class RpcReferenceBeanDefinitionParser implements BeanDefinitionParser {
    private ConfigTypeEnum configTypeEnum;

    public RpcReferenceBeanDefinitionParser(ConfigTypeEnum configTypeEnum) {
        this.configTypeEnum = configTypeEnum;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        return null;
    }
}
